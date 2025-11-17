defmodule BankAccount do
  @moduledoc """
  A bank account that supports access from multiple processes.
  """
  use Agent

  @enforce_keys [:balance]
  defstruct [:balance]

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
  This is irreversible.  Data will be irrecoverably lost.

  WARNING: Should the pid be recycled, there will be no way to distinguish
  transactions intended for the old account.
  """
  @spec close_bank(account) :: none
  def close_bank(account_pid),
    do: Agent.stop(account_pid)

  @doc """
  Get the account's balance.
  """
  @spec balance(account) :: integer
  def balance(%BankAccount{balance: balance}),
    do: balance

  def balance(@account_closed),
    do: @account_closed

  def balance(account_pid),
    do:
      account_pid
      |> get_account()
      |> balance()

  @doc """
  Update the account's balance by adding the given amount which may be negative.
  """
  @spec update(account, integer) :: any
  def update(account_pid, amount),
    do:
      account_pid
      |> get_account()
      |> update(account_pid, amount)

  def update(
        %BankAccount{balance: balance},
        account_pid,
        amount
      ),
      do: Agent.update(account_pid, &%{&1 | balance: balance + amount})

  def update(
        @account_closed,
        _account_pid,
        _amount
      ),
      do: @account_closed

  defp get_account(account_pid) do
    try do
      Agent.get(account_pid, & &1)
    catch
      :exit, _ ->
        @account_closed
    end
  end
end
