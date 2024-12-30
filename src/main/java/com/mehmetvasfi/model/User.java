package com.mehmetvasfi.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> ownerFiles;

    @ManyToMany
    @JoinTable(name = "user_accessible_files", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "file_id"))
    private List<FileEntity> accessibleFiles;

}