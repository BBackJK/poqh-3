package bback.module.poqh3;

import java.util.List;
import java.util.Optional;

public interface QueryResultHandler<T> {


    List<T> list(String query);
    Optional<T> detail(String query);
}
