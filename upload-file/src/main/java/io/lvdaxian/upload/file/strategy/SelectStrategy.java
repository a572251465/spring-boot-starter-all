package io.lvdaxian.upload.file.strategy;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.functional.ConsumerP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SelectStrategy extends ConsumerP<HttpServletRequest, HttpServletResponse, FileOperate> {

}
