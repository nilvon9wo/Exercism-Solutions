import java.util.Optional;

class ErrorHandling {
	void handleErrorByThrowingIllegalArgumentException() {
		throw new IllegalArgumentException();
	}

	@SuppressWarnings("SameParameterValue")
	void handleErrorByThrowingIllegalArgumentExceptionWithDetailMessage(String message) {
		throw new IllegalArgumentException(message);
	}

	void handleErrorByThrowingAnyCheckedException() throws Exception {
		throw new Exception();
	}

	@SuppressWarnings("SameParameterValue")
	void handleErrorByThrowingAnyCheckedExceptionWithDetailMessage(String message) throws Exception {
		throw new Exception(message);
	}

	void handleErrorByThrowingAnyUncheckedException() {
		throw new RuntimeException();
	}

	@SuppressWarnings("SameParameterValue")
	void handleErrorByThrowingAnyUncheckedExceptionWithDetailMessage(String message) {
		throw new RuntimeException(message);
	}

	void handleErrorByThrowingCustomCheckedException() throws CustomCheckedException {
		throw new CustomCheckedException();
	}

	@SuppressWarnings("SameParameterValue")
	void handleErrorByThrowingCustomCheckedExceptionWithDetailMessage(String message) throws CustomCheckedException {
		throw new CustomCheckedException(message);
	}

	void handleErrorByThrowingCustomUncheckedException() {
		throw new CustomUncheckedException();
	}

	@SuppressWarnings("SameParameterValue")
	void handleErrorByThrowingCustomUncheckedExceptionWithDetailMessage(String message) {
		throw new CustomUncheckedException(message);
	}

	Optional<Integer> handleErrorByReturningOptionalInstance(String integer) {
		try {
			return Optional.of(Integer.parseInt(integer));
		}
		catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
}