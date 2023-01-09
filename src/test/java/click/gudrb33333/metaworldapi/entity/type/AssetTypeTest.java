package click.gudrb33333.metaworldapi.entity.type;

import static org.assertj.core.api.Assertions.assertThat;

import click.gudrb33333.metaworldapi.entity.type.AssetType.Values;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetTypeTest {

  @InjectMocks Values values;

  @Test
  void assetTypeValue() {
    assertThat(Values.AVATAR).isEqualTo("AVATAR");
    assertThat(Values.CLOTHING).isEqualTo("CLOTHING");
  }
}
