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
    {:ok, pid} = Agent.start_link(&create_account/0)
    pid
  end

  defp create_account,
    do: %BankAccount{balance: 0}

  @doc """
  Close the bank. Makes the account unavailable.
  """
  @spec close_bank(account) :: none
  def close_bank(account_pid),
    do: Agent.update(account_pid, &%{&1 | status: :closed})

  @doc """
  Get the account's balance.
  """
  @spec balance(account) :: integer
  def balance(%BankAccount{balance: balance, status: :open}),
    do: balance

  def balance(%BankAccount{status: :closed}),
    do: @account_closed

  def balance(account_pid),
    do: balance(get_account(account_pid))

  @doc """
  Update the account's balance by adding the given amount which may be negative.
  """
  @spec update(account, integer) :: any
  def update(account_pid, amount),
    do: update(account_pid, amount, get_account(account_pid))

  def update(
        account_pid,
        amount,
        %BankAccount{balance: balance, status: :open}
      ),
      do: Agent.update(account_pid, &%{&1 | balance: balance + amount})

  def update(
        _account_pid,
        _amount,
        %BankAccount{status: :closed}
      ),
      do: @account_closed

  defp get_account(account_pid),
    do: Agent.get(account_pid, & &1)
end
