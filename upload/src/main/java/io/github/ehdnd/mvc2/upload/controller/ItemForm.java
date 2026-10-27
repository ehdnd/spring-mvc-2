package io.github.ehdnd.mvc2.upload.controller;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {

  private Long id;
  private String itemName;
  private MultipartFile attachFile;
  private List<MultipartFile> imageFiles;
}
