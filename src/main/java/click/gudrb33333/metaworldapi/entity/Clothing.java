package click.gudrb33333.metaworldapi.entity;

import click.gudrb33333.metaworldapi.entity.type.AssetType.Values;
import click.gudrb33333.metaworldapi.entity.type.GenderType;
import click.gudrb33333.metaworldapi.entity.type.GenderTypeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "clothing")
@DiscriminatorValue(Values.CLOTHING)
public class Clothing extends Asset{

    private String name;

    private String brand;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "gender_type")
    @Convert(converter = GenderTypeConverter.class)
    private GenderType genderType;

    private int price;

    @Column(name = "associate_link")
    private String associateLink;

    @Column(name = "detail_description", columnDefinition = "TEXT")
    private String detailDescription;
}
