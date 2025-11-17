defmodule BankAccount do
  @moduledoc """
  A bank account that supports access from multiple processes.
  """
  use Agent

  @enforce_keys [:balance]
  defstruct [:balance, status: :open]

  @account_closed {:error, :account_closed}

  @typedoc """
  An account handle.
  """
  @opaque account :: pid

  @doc """
  Open the bank. Makes the account available.
  """
  @spec open_bank() :: account
  def open_bank() do
    account = %BankAccount{balance: 0}
    Agent.start_link(fn -> account end, name: __MODULE__)
  end

  @doc """
  Close the bank. Makes the account unavailable.
  """
  @spec close_bank(account) :: none
  def close_bank(_account) do
    Agent.update(__MODULE__, &%{&1 | status: :closed})
  end

  @doc """
  Get the account's balance.
  """
  @spec balance(account) :: integer
  def balance(%BankAccount{balance: balance, status: :open}),
    do: balance

  def balance(%BankAccount{status: :closed}),
    do: @account_closed

  def balance(_account),
    do: balance(get_account())

  @doc """
  Update the account's balance by adding the given amount which may be negative.
  """
  @spec update(account, integer) :: any
  def update(%BankAccount{balance: balance, status: :open}, amount),
    do: Agent.update(__MODULE__, &%{&1 | balance: balance + amount})

  def update(%BankAccount{status: :closed}, _amount),
    do: @account_closed

  def update(_account, amount),
    do: update(get_account(), amount)

  defp get_account,
    do: Agent.get(__MODULE__, & &1)
end
