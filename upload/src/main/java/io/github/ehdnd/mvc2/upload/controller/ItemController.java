package io.github.ehdnd.mvc2.upload.controller;

import io.github.ehdnd.mvc2.upload.domain.Item;
import io.github.ehdnd.mvc2.upload.domain.ItemRepository;
import io.github.ehdnd.mvc2.upload.domain.UploadFile;
import io.github.ehdnd.mvc2.upload.file.FileStore;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemRepository itemRepository;
  private final FileStore fileStore;

  @GetMapping("/items/new")
  public String newItem(@ModelAttribute ItemForm form) {
    return "item-form";
  }

  @PostMapping("/items/new")
  public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes)
      throws IOException {

    UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
    List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

    // 보통 S3에 실제 파일 저장하고 DB에는 경로 / 이름만 저장한다.
    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setAttachFile(attachFile);
    item.setImageFiles(storeImageFiles);
    itemRepository.save(item);

    redirectAttributes.addAttribute("itemId", item.getId());

    return "redirect:/items/{itemId}";
  }

  @GetMapping("/items/{id}")
  public String items(@PathVariable Long id, Model model) {

    Item item = itemRepository.findById(id);
    model.addAttribute("item", item);
    return "item-view";
  }

  @ResponseBody
  @GetMapping("/images/{filename}")
  public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
    return new UrlResource("file:" + fileStore.getFullPath(filename));
  }

  @GetMapping("/attach/{itemId}")
  public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
      throws MalformedURLException {
    // itemId 를 받아서 인가된 사용자만 다운받을 수 있도록 해야지.
    Item item = itemRepository.findById(itemId);
    String storeFileName = item.getAttachFile().getStoreFileName();
    String uploadFileName = item.getAttachFile().getUploadFileName();

    UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

    log.info("uploadFileName={}", uploadFileName);

    String encodedFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

    // 이래야 브라우저에서 파일을 여는게 아닌 다운로드가 된다.
    String contentDisposition = "attachmen; filename=\"" + encodedFileName + "\"";
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
        .body(urlResource);
  }
}
