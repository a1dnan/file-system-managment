package com.a1dnan.filesys.repositories;

import com.a1dnan.filesys.entities.FileData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileDataRepository extends JpaRepository<FileData, Long> {

}
