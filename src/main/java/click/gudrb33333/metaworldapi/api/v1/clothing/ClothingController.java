package click.gudrb33333.metaworldapi.api.v1.clothing;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"의상 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clothing")
public class ClothingController {}
