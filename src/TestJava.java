import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cdesilva on 18/8/17.
 */
public class TestJava
{


    public static void main(String args[]) {
        System.out.println("hellow ");

        final ExecutorService executor1 = Executors.newFixedThreadPool(10, new ThreadFactoryBuilder().setNameFormat("SubscribeOn-%d").build());

        List<Long> ss = new ArrayList<>();
        ss.add(1212L);
        ss.add(1213L);
        ss.add(1214L);



        Observable.from(ss).subscribeOn(Schedulers.from(executor1))

                .flatMap(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        System.out.println("AAAA >>"+aLong);

                        System.out.println(String.format("execute thread: %s ",
                                Thread.currentThread().getName()));


                        try {
                            if (aLong.toString().equals("1213"))
                                throw new Exception();
                        } catch (Throwable e ){
                            return Observable.empty();
                        }

                        return Observable.just(aLong).subscribeOn(Schedulers.from(executor1));
                    }
                }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.err.print(e);
            }

            @Override
            public void onNext(Long s) {
                System.out.println("on next >>"+s.toString());
            }
        });

//                .map(new Func1<Long, String>() {
//
//                    @Override
//                    public String call(Long aLong) {
//                        System.out.println("hellow 1>>"+aLong);
//                        try {
//                            throw new Exception();
//                        } catch (Throwable e ){
//                           Exceptions.propagate(e);
//                        }
//
//                        return aLong.toString();
//                    }
//                })
//                .map(new Func1<Long, String>() {
//                    @Override
//                    public String call(Long s) {
//                        System.out.println("BBBBB >>"+s.toString());
//                        return s.toString();
//                    }
//                })
//                .onErrorResumeNext(new Func1<Throwable, Observable<String>>() {
//                        @Override
//                        public Observable<String> call(Throwable throwable) {
//                            return Observable.empty();
//                        }
//                 })
////                .onErrorReturn(new Func1<Throwable, String>() {
////                    @Override
////                    public String call(Throwable throwable) {
////                        return null;
////                    }
////                })
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.err.print(e);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("on next >>"+s);
//                    }
//                });
    }
}
