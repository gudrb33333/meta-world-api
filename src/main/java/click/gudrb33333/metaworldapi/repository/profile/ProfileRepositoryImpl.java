package click.gudrb33333.metaworldapi.repository.profile;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
