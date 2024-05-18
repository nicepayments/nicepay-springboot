package kr.co.nicepay.untact.common.functions;

@FunctionalInterface
public interface TripleConsumer<T, U, V> {
  void accept(T t, U u, V v);
}