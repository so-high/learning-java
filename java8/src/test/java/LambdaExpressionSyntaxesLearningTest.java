import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Thread.*;

/**
 * Created by colin on 15. 8. 15..
 */
public class LambdaExpressionSyntaxesLearningTest {
	private List<String> listOfStrings;
	@Before
	public void prepare(){
		listOfStrings = Arrays.asList(new String[]{"1","21","321","4321","54321"});
	}

	@Test
	public void parameterTypesCanBeInferredInContext(){
		listOfStrings.sort((first, second) -> Integer.compare(first.length(), second.length()));
	}

	@Test
	public void parenthesisCanBeOmittedWhenLambdaBlockRequiresOnlyOneParameter(){
		listOfStrings.stream().map(anElement -> anElement.toUpperCase());
	}

	@Test
	public void isLambdaExpressionJustSyntacticSugarForAnonymousImplementation(){
		// List.sort only accepts Comparator Instance

		Comparator<String> expectComparatorInstanceAlso = Discloser.acceptComparatorAndRuturnIt((first, second) -> 0);
		Comparator<String> expectComparatorInstance = Discloser.acceptComparatorAndRuturnIt(new Comparator<String>() {
			@Override public int compare(String first, String second) {
				return first.compareTo(second);
			}
		});
	}

	@Test
	public void feelLikeYesButDoMore(){
		/*
			Just from a lambda expression conforming Comparator.compare() method signature,
			we can get Comparator instance.
		*/
		Comparator<String> expectComparatorMadeDirectlyFromLambdaExpression = (first, second) -> 0;

		/*
			and naturally, a lambda expressions have to correspond to a signature of single abstract method
			which is to be implemented from it.
		 */
		Runnable runnableFromLambda = () -> {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				//Can't throw any checked exceptions as Runnable::run method doesn't have throws definition.
			}
		};
		Callable<?> callableFromLambda = () -> {Thread.sleep(10); return null;};
	}

	@Test
	public void methodReferenceDoMore() {
		//with instance::instanceMethod : we can just reuse code
		listOfStrings.sort((new ImNotAClassImplementingComparator<>())::accept2ArgsAndReturn0);

		//with Class::staticMethod : we can just reuse code
		listOfStrings.sort(ImNotAClassImplementingComparator::accept2ArgsAndReturn0Static);

		/*
		with Class::instanceMethod,
			- allow to re-use pre-defined methods
			- gets boiler plate code less
			- besides, make the implementation and binding code less
		*/
		listOfStrings.sort(String::compareTo);
		// the above is same as below
		listOfStrings.sort(new Comparator<String>() {
			@Override public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
	}


	public static class Discloser {
		public static <E> Comparator<E> acceptComparatorAndRuturnIt(Comparator<E> comparator){
			return comparator;
		}
	}

	public static class ImNotAClassImplementingComparator<T>{
		public static <E> int accept2ArgsAndReturn0Static(E o1, E o2) {
			return 0;
		}
		public int accept2ArgsAndReturn0(T o1, T o2) {
			return 0;
		}
	}
}
