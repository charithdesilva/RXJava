import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main1 {

    private static final ExecutorService executor1 = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNameFormat("SubscribeOn-%d").build());
    private static final Scheduler schedulers = Schedulers.immediate();

    public static void main(String[] arguments) throws Throwable {
//        getPages(5).flatMap(  page -> actOnAPage(page).subscribeOn(Schedulers.from(executor1)) )
//                .subscribe(s -> {
//                    System.out.println("Completed Processing page: {}"+ s);
//                });

        getPages1(5).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {

                sleepQuietly(1, TimeUnit.SECONDS);
                System.out.println(String.format("execute thread: %s ",
                        Thread.currentThread().getName()));
                System.out.println("Acting on page {}" +  integer);

                return actOnAPage(integer).subscribeOn(schedulers);
            }
        }).subscribe(s -> {
            System.out.println("Completed Processing page: {}"+ s);
        });
        ;

//
//        getPages1(5).flatMap(  page -> actOnAPage(page).subscribeOn(Schedulers.from(executor1)) )
//                .subscribe(s -> {
//                    System.out.println("Completed Processing page: {}"+ s);
//                });
        System.out.println("finish");
    }

    public static Observable<Integer> getPages(int totalPages) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("Getting pages");
                for (int i = 1; i <= totalPages; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<Integer> getPages1(int totalPages) {

        final List<Integer> inputs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        return Observable.from(inputs);

//        return Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                System.out.println("Getting pages");
//                for (int i = 1; i <= totalPages; i++) {
//                    subscriber.onNext(i);
//                }
//                subscriber.onCompleted();
//            }
//        });
    }

    public static void sleepQuietly(int duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {

        }
    }

    public static Observable<String> actOnAPage(int pageNum) {
        return Observable.<String>create(s -> {

            sleepQuietly(2, TimeUnit.SECONDS);
            System.out.println(String.format("execute thread: %s ",
                    Thread.currentThread().getName()));
            System.out.println("Acting on page {}" +  pageNum);
            s.onNext("Page " + pageNum);
            s.onCompleted();
        });
    }




}