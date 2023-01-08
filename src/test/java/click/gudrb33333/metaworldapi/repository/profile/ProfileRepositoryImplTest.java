package click.gudrb33333.metaworldapi.repository.profile;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileRepositoryImplTest {

  @InjectMocks ProfileRepositoryImpl profileRepositoryImpl;

  @Mock JPAQueryFactory queryFactory;
}
