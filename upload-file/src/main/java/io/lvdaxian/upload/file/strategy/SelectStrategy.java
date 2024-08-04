package io.lvdaxian.upload.file.strategy;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.functional.ConsumerP;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface SelectStrategy extends ConsumerP<HttpServletRequest, FileOperate, ResponseEntity> {

}
