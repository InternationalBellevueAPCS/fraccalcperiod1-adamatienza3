import java.util.*;
import java.lang.*;

public class FracCalc {

	/**
	 * Prompts user for input, passes that input to produceAnswer, then outputs the
	 * result.
	 * 
	 * @param args - unused
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// TODO: Read the input from the user and call produceAnswer with an equation
		// Checkpoint 1: Create a Scanner, read one line of input, pass that input to
		// produceAnswer, print the result.
		// Checkpoint 2: Accept user input multiple times.
		boolean done = false;
	while (!done) {
		System.out.print("(type \"quit\" to stop) ");
		String input = scanner.nextLine();

		if (input.equals("quit"))
			done = true;

		if (!done) {
			System.out.println(produceAnswer(input));
		}
	} 
	}


	/**
	 * produceAnswer - This function takes a String 'input' and produces the result.
	 * 
	 * @param input - A fraction string that needs to be evaluated. For your
	 *              program, this will be the user input. Example: input ==> "1/2 +
	 *              3/4"
	 * @return the result of the fraction after it has been calculated. Example:
	 *         return ==> "1_1/4"
	 */
	public static String produceAnswer(String input) {
		boolean addition = false;
		boolean subtraction = false;
		boolean multiplication = false;
		boolean division = false;

		String first;
		String operator;
		String second;

		int index = input.indexOf(' ');
		first = input.substring(0, index);
		input = input.substring(index + 1);

		index = input.indexOf(' ');
		operator = input.substring(0, index);
		input = input.substring(index + 1);

		second = input;

		addition = operator.equals("+");
		subtraction = operator.equals("-");
		multiplication = operator.equals("*");
		division = operator.equals("/");

		int[] firstNumber = returnNumberFromString(first);
		int[] secondNumber = returnNumberFromString(second);
		int[] answer = new int[3];

		if (addition) {
			answer = addition(firstNumber, secondNumber);
		} else if (subtraction) {
			answer = subtraction(firstNumber, secondNumber);
		} else if (multiplication) {
			answer = multiplication(firstNumber, secondNumber);
		} else if (division) {
			answer = division(firstNumber, secondNumber);
		}

		int[] simplified = simplify(answer);
		
		if (simplified[1] < 0 && simplified[0] != 0) {
			simplified[1] = Math.abs(simplified[1]);
		}
		if (simplified[2] < 0) {
			simplified[2] = Math.abs(simplified[2]);
		}

		String value = "";

		if (simplified[0] != 0 && simplified[1] != 0) {
			value += simplified[0] + "_" + simplified[1] + "/" + simplified[2];
		} else if (simplified[1] != 0 && simplified[2] != 1) {
			value += simplified[1] + "/" + simplified[2];
		} else if (simplified[0] != 0) {
			value += simplified[0];
		} else {
			value = "0";
		}

		return value;
	}
	
	public static int[] returnNumberFromString(String input) {
		int[] number = new int[3];
		number[1] = 0;
		number[2] = 1;

		if (input.contains("_") && input.contains("/")) {
			int index = input.indexOf('_');
			number[0] = Integer.parseInt(input.substring(0, index));
			input = input.substring(index + 1);
		} else if (!input.contains("_") && !input.contains("/")) {
			
			number[0] = Integer.parseInt(input);
		}

		if (input.contains("/")) {
			int index = input.indexOf('/');
			number[1] = Integer.parseInt(input.substring(0, index));
			number[2] = Integer.parseInt(input.substring(index + 1));
		}

		if (number[0] != 0)
			number[1] = Math.abs(number[1]);

		return number;
	}
	public static int[] addition(int[] firstNumber, int[] secondNumber) {
		int[] answer = new int[3];
		
		boolean addition = false;
		boolean negative = false;

		
		if (!isItNegative(firstNumber) && !isItNegative(secondNumber)) {
			addition = true;
		}

	
		else if (isItNegative(firstNumber) && isItNegative(secondNumber)) {
			addition = true;
			negative = true;
			Math.abs(firstNumber[0]);
			Math.abs(firstNumber[1]);
			Math.abs(secondNumber[0]);
			Math.abs(secondNumber[1]);
		}

		if (addition) {
			
			answer[0] = firstNumber[0] + secondNumber[0];
			answer[2] = firstNumber[2] * secondNumber[2];
			answer[1] = (firstNumber[1] * secondNumber[2]) + (secondNumber[1] * firstNumber[2]);
		} else {
			int[] positiveNumber = new int[3];
			int[] negativeNumber = new int[3];
		
			if (firstNumber[0] < 0 || firstNumber[1] < 0) {
				negativeNumber = positive(firstNumber);
				positiveNumber = secondNumber;
			} else {
			
				negativeNumber = positive(secondNumber);
				positiveNumber = firstNumber;
			}
			answer = subtraction(positiveNumber, negativeNumber);
		}

		if (negative) {
			answer = negative(answer);
		}

		return answer;
	}

	public static int[] subtraction(int[] firstNumber, int[] secondNumber) {
		boolean subtraction = false;
		boolean bothNegative = false;
		int[] answer = new int[3];

		if (!isItNegative(firstNumber) && !isItNegative(secondNumber)) {
			subtraction = true;
		} else if (isItNegative(firstNumber) && isItNegative(secondNumber)) {
			subtraction = true;
			bothNegative = true;
		} else if (isItNegative(firstNumber)) {
			answer = addition(firstNumber, negative(secondNumber));
		} else {
			answer = addition(firstNumber, positive(secondNumber));
		}

		if (subtraction) {
			
			int[] positiveNumber = greater(positive(firstNumber), positive(secondNumber));
			int[] negativeNumber = less(positive(firstNumber), positive(secondNumber));

			
			boolean turnNegativeAtEnd = false;
			if (Arrays.equals(positiveNumber, positive(secondNumber)) && !bothNegative) {
				turnNegativeAtEnd = true;
			} else if (Arrays.equals(positiveNumber, positive(firstNumber)) && bothNegative) {
				turnNegativeAtEnd = true;
			}
			
			answer[2] = positiveNumber[2];
			if (positiveNumber[2] != negativeNumber[2]) {
				int firstDen = positiveNumber[2] * negativeNumber[2];
				int secondDen = positiveNumber[2] * negativeNumber[2];
				int firstNum = positiveNumber[1] * negativeNumber[2];
				int secondNum = positiveNumber[2] * negativeNumber[1];
				answer[2] = positiveNumber[2] * negativeNumber[2];

				positiveNumber[2] = firstDen;
				negativeNumber[2] = secondDen;
				positiveNumber[1] = firstNum;
				negativeNumber[1] = secondNum;
			}

			answer[0] = positiveNumber[0] - negativeNumber[0];
			
			if (positiveNumber[1] >= negativeNumber[1]) {
				answer[1] = positiveNumber[1] - negativeNumber[1];
			}
		
			else {
				answer[0]--;
				answer[1] = (positiveNumber[1] + answer[2]) - negativeNumber[1];
			}

			
			if (turnNegativeAtEnd) {
				answer = negative(answer);
			}
		}
		return answer;
	}

	public static int[] multiplication(int[] firstNumber, int[] secondNumber) {
		
		int[] answer = new int[3];
		boolean positive = true;

		if ((!isItNegative(firstNumber) && isItNegative(secondNumber))
				|| (isItNegative(firstNumber) && !isItNegative(secondNumber))) {
			positive = false;
		}

		firstNumber = positive(firstNumber);
		secondNumber = positive(secondNumber);

		answer[1] = (firstNumber[1] + firstNumber[0] * firstNumber[2])
				* (secondNumber[1] + secondNumber[0] * secondNumber[2]);
		answer[2] = firstNumber[2] * secondNumber[2];

		if (!positive) {
			answer = negative(answer);
		}
		return answer;
	}

	public static int[] division(int[] firstNumber, int[] secondNumber) {

		int[] answer = new int[3];
		boolean positive = true;
		if ((!isItNegative(firstNumber) && isItNegative(secondNumber))
				|| (isItNegative(firstNumber) && !isItNegative(secondNumber))) {
			positive = false;
		}
		firstNumber = positive(firstNumber);
		secondNumber = positive(secondNumber);
		answer[1] = (firstNumber[1] + firstNumber[0] * firstNumber[2]) * secondNumber[2];
		answer[2] = firstNumber[2] * (secondNumber[1] + secondNumber[0] * secondNumber[2]);
		if (!positive) {
			answer = negative(answer);
		}

		return answer;
	}

	public static int[] simplify(int[] number) {

		int gcd = greatestCommonDivisor(number[1], number[2]);
		number[1] /= gcd;
		number[2] /= gcd;

		if (isItNegative(number) && number[1] > 0) {
			number[0] -= number[1] / number[2];
		} else {
			number[0] += number[1] / number[2];
		}
		number[1] = number[1] % number[2];

		return number;
	}
	
	public static int[] less(int[] firstNumber, int[] secondNumber) {
		
		int[] number;
		if (firstNumber[0] < secondNumber[0]) {
			number = firstNumber;
		} else if (firstNumber[0] > secondNumber[0]) {
			number = secondNumber;
		} else if (firstNumber[1] < secondNumber[1]) {
			number = firstNumber;
		} else if (firstNumber[1] > secondNumber[1]) {
			number = secondNumber;
		} else {
			number = firstNumber;
		}
		return number;
	}

	public static int[] greater(int[] firstNumber, int[] secondNumber) {

		int[] number;
		if (firstNumber[0] > secondNumber[0]) {
			number = firstNumber;
		} else if (firstNumber[0] < secondNumber[0]) {
			number = secondNumber;
		} else if (firstNumber[1] > secondNumber[1]) {
			number = firstNumber;
		} else if (firstNumber[1] < secondNumber[1]) {
			number = secondNumber;
		} else {
			number = firstNumber;
		}
		return number;
	}
	
	public static boolean isItNegative(int[] number) {
		return number[0] < 0 || number[1] < 0;
	}

	public static int[] positive(int[] number) {
		
		number[0] = Math.abs(number[0]);
		number[1] = Math.abs(number[1]);
		return number;
	}

	public static int[] negative(int[] number) {
		
		number[0] = Math.abs(number[0]) * -1;
		if (number[0] == 0) {
			number[1] = Math.abs(number[1]) * -1;
		}

		return number;
	}

	// TODO: Fill in the space below with helper methods

	/**
	 * greatestCommonDivisor - Find the largest integer that evenly divides two
	 * integers. Use this helper method in the Final Checkpoint to reduce fractions.
	 * Note: There is a different (recursive) implementation in BJP Chapter 12.
	 * 
	 * @param a - First integer.
	 * @param b - Second integer.
	 * @return The GCD.
	 */
	public static int greatestCommonDivisor(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		int max = Math.max(a, b);
		int min = Math.min(a, b);
		while (min != 0) {
			int tmp = min;
			min = max % min;
			max = tmp;
		}
		return max;
	}

	/**
	 * leastCommonMultiple - Find the smallest integer that can be evenly divided by
	 * two integers. Use this helper method in Checkpoint 3 to evaluate expressions.
	 * 
	 * @param a - First integer.
	 * @param b - Second integer.
	 * @return The LCM.
	 */
	public static int leastCommonMultiple(int a, int b) {
		int gcd = greatestCommonDivisor(a, b);
		return (a * b) / gcd;
	}
}
