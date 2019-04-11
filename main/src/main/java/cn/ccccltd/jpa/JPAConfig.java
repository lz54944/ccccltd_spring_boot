package cn.ccccltd.jpa;

import cn.ccccltd.beans.User;
import cn.ccccltd.common.utils.UserUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JPAConfig {
	@Bean
	public AuditorAware<User> auditorAware() {
		return new AuditorAware<User>() {

			@Override
			public Optional<User> getCurrentAuditor() {
				//System.out.println("\n\nJPAConfig.auditorAware().new AuditorAware() {...}.getCurrentAuditor()");

				// 后台任务，不需要登录
				// TODO 后台创建的生活，可能就会为空
				if (JPAThreadLocal.background()){
					return null;
				} else {
					return Optional.of(UserUtil.getUser());
				}
			}
		};
	}
}