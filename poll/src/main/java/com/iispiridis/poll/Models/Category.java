package com.iispiridis.poll.Models;


import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private CategoryName name;

    public void setId(Long id) {
        this.id = id;
    }

    public Category(String name)
    {
        this.name = CategoryName.valueOf(name);
    }

    public CategoryName getName() {
        return name;
    }

    public void setName(CategoryName name) {
        this.name = name;
    }


    public Category() {
    }

    public Category(CategoryName name)
    {
        this.name = name;
    }

    public Long getId() {
        return id;
    }


}
