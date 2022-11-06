package platform;

import org.springframework.data.repository.CrudRepository;

public interface CodeRepository extends CrudRepository<Code, Integer> {
    Code findByUuid(String uuid);
}
