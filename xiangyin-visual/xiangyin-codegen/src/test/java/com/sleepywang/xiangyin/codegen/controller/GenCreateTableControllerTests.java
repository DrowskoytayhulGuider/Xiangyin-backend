package com.sleepywang.xiangyin.codegen.controller;

import com.sleepywang.xiangyin.codegen.service.GenCreateTableService;
import com.sleepywang.xiangyin.codegen.util.vo.GenCreateTableVO;
import com.sleepywang.xiangyin.common.core.util.R;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class GenCreateTableControllerTests {

	@Test
	void rejectsInjectedTableNameBeforeCreatingTable() {
		GenCreateTableService service = mock(GenCreateTableService.class);
		GenCreateTableController controller = new GenCreateTableController(service);
		GenCreateTableVO request = new GenCreateTableVO();
		request.setDsName("master");
		request.setTableName("sys_user WHERE 1=EXTRACTVALUE(1,CONCAT(0x7e,user(),0x7e))#");

		R result = controller.save(request);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).isEqualTo("非法内容");
		verifyNoInteractions(service);
	}

}
