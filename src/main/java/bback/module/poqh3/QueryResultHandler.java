package bback.module.poqh3;

import java.util.List;
import java.util.Optional;

public interface QueryResultHandler<R> {


    List<R> list(String query);
    Optional<R> detail(String query);
}
