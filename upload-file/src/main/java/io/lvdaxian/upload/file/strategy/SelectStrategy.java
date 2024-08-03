package io.lvdaxian.upload.file.strategy;

import javax.servlet.http.HttpServletResponse;
import java.util.function.Consumer;

public interface SelectStrategy extends Consumer<HttpServletResponse> {
}
