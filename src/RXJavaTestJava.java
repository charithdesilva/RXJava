package au.necdl.pexa.web.document;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.schedulers.Schedulers;

public class RXJavaTestJava {

    public static Random RANDOM = new Random();
    public static StopWatch sw = new StopWatch();


//
//    public static void main(String[] arguments) throws Throwable {
//        sw.start();
//        final List<Integer> inputs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,18, 19, 20);
//        final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(20);
//
//        Observable.from(inputs)//.observeOn(Schedulers.immediate())
//                .flatMap((Integer input) -> deferHeavyWeightStuff(input).subscribeOn(Schedulers.from(threadPoolExecutor)))
//
//                .subscribe(s -> {
//                    System.out.println("Completed Processing page : >>>>"+ s);
//                }, Throwable::printStackTrace, () -> {sw.stop(); System.out.println("FINALLY ");});
//
//        //threadPoolExecutor.shutdown();
//    }


    public static void main(String[] arguments) throws Throwable {
        final List<Integer> inputs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(1000);

        Iterable<Integer> outputs = Observable.<Integer>from(inputs)
        .observeOn(Schedulers.immediate())
                .flatMap((Integer input) -> deferHeavyWeightStuff(input).subscribeOn(Schedulers.io()))
                .toBlocking()
                .toIterable();

        for (Integer output : outputs) {
            System.out.println("received item >>>>" +output);
        }

        threadPoolExecutor.shutdown();
    }

    public static void sleepQuietly(int duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {

        }
    }

    public static Observable<Integer> deferHeavyWeightStuff(final int input) {
        //return Observable.just(doHeavyWeightStuff(input));
        return Observable.defer(() -> Observable.just(doHeavyWeightStuff(input)));
    }

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static int doHeavyWeightStuff(int input) {
        System.out.println(String.format("execute thread: %s ",
                Thread.currentThread().getName()));
        if (input == 2 ||input == 10 ) {
            sleepQuietly(10, TimeUnit.SECONDS);
        } else {
            sleepQuietly(randomInt(2,5), TimeUnit.SECONDS);
        }

        //int output = (int) Math.pow(input, 2);
        return input;
    }

}
