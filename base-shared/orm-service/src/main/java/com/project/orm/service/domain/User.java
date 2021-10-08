package com.project.orm.service.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :07/10/2021 - 5:24 PM
 */

@Entity
@Table(name = "tb_user")
@Data
public class User {
    private Long id;
    private String name;
}
