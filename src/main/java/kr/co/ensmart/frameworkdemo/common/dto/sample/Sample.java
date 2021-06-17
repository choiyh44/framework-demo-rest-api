package kr.co.ensmart.frameworkdemo.common.dto.sample;

import org.apache.ibatis.type.Alias;

import kr.co.ensmart.frameworkdemo.common.dto.base.BaseCommonEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("sample")
public class Sample extends BaseCommonEntity {
	private static final long serialVersionUID = 2541293207832550448L;
	private Integer id;
	private String name;
	private String description;
}
