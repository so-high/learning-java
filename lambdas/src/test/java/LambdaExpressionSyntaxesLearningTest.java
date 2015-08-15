import org.junit.Assert;
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
	private List<String> strings;
	private List<Integer> integers;
	@Before
	public void prepare(){
		strings = Arrays.asList(new String[]{"1","21","321","4321","54321"});
		integers = Arrays.asList(new Integer[]{1,2,3,4,5});
	}

	@Test
	public void parameterTypesCanBeInferredByDefinition(){
		strings.sort((first, second) -> Integer.compare(first.length(), second.length()));
	}

	@Test
	public void parenthesisCanBeOmittedWhenRequireOnlyOneParameter(){
		strings.stream().map(anElement -> anElement.toUpperCase());
	}

	@Test
	public void isLambdaExpressionJustSyntacticSugarForAnonymousImplementation(){
		Comparator lambdaExpressionInstance = LambdaExpressionDiscloser.disclose(
			(Integer first, Integer second) -> Integer.compare(first, second));
		Comparator anonymousComparatorInstance= LambdaExpressionDiscloser.disclose(new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
				return 0;
			}
		});
		Comparator namedComparatorInstance= LambdaExpressionDiscloser.disclose(new NamedComparator());
		Assert.assertTrue(Comparator.class.isAssignableFrom(lambdaExpressionInstance.getClass()));
		Assert.assertTrue(Comparator.class.isAssignableFrom(anonymousComparatorInstance.getClass()));
		Assert.assertTrue(Comparator.class.isAssignableFrom(namedComparatorInstance.getClass()));
	}

	@Test
	public void seemsYesSoNaturallyHaveToHaveConsistentMethodSignature(){
		Runnable runnableFromLambda = () -> {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				//Can't throw Checked Exception as Runnable::run method doesn't have throws definition.
			}
		};

		Callable<?> callableFromLambda = () -> {Thread.sleep(10);return null;};
	}

	@Test
	public void butMethodReferenceProvideMoreSyntacticSugar() {
		Comparator<String> comparatorFromStringCompareToMethod = LambdaExpressionDiscloser.disclose(String::compareTo);
		/*
		The above is same as the below

		Comparator<String> comparator = new Comparator<String>(){
			public int compare(String s1, String s2){
				return s1.compareTo(s2);
			}
		}
		*/
	}



	public static class LambdaExpressionDiscloser{
		public static <E> Comparator<E> disclose(Comparator<E> comparator){
			return comparator;
		}

		public static <E> Comparator<E> createComplareFrom(Comparator<E> comparator){
			return comparator;
		}
	}
	public static class NamedComparator implements Comparator<Integer>{
		@Override public int compare(Integer o1, Integer o2) {
			return 0;
		}
	}

}
