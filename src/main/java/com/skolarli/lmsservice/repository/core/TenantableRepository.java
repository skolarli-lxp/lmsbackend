package com.skolarli.lmsservice.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, Long> {
}
