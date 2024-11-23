package com.daniilSergachev.CloudFileStorage.controllers.MinioControllers;

import com.daniilSergachev.CloudFileStorage.validator.form.FolderForm;
import com.daniilSergachev.CloudFileStorage.dto.FolderUpdateRequest;
import com.daniilSergachev.CloudFileStorage.dto.RenameFileRequest;
import com.daniilSergachev.CloudFileStorage.dto.RequestDeleteFile;
import com.daniilSergachev.CloudFileStorage.model.User;
import com.daniilSergachev.CloudFileStorage.services.BreadcrumbService;
import com.daniilSergachev.CloudFileStorage.services.MinioService;
import com.daniilSergachev.CloudFileStorage.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/minio")
@CrossOrigin
public class MinioController {

    private final MinioService minioService;

    private final UserService userService;




    @PostMapping("/files/upload")
    public ResponseEntity<String> uploadFile(MultipartFile file,
                                             Principal principal,
                                             @RequestParam String currentPath) {

        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String objectName = file.getOriginalFilename();
        minioService.uploadFile(file, currentPath, objectName, user.getId());
        return ResponseEntity.ok(objectName + "Success upload");

    }




    @PostMapping("/create-folder")
    public ResponseEntity<String> newFolder(
            @Valid @RequestBody FolderForm folderForm, Principal principal) {

        User user = userService.findByUsername(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        String path = folderForm.getPath() == null ? "" : folderForm.getPath();
        minioService.createFolder(path, folderForm.getName(), user.getId());

        return ResponseEntity.ok("Successfully created folder");
    }




    @PostMapping("/files/update")
    public ResponseEntity<String> renameFile(@Valid @RequestBody RenameFileRequest renameFileRequest,
                                             Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String oldNameFile = renameFileRequest.getOldNameFile();
        String newNameFile = renameFileRequest.getNewNameFile();

        String path = renameFileRequest.getCurrentPath() == null ? "" : renameFileRequest.getCurrentPath();


        if (!newNameFile.equals(oldNameFile)) {
            minioService.copy(newNameFile, oldNameFile, path, user.getId());
            minioService.delete(oldNameFile, path, user.getId(), false);
        }

        return ResponseEntity.ok("Successfully renamed file");
    }


    @PostMapping("/folders/update")
    public ResponseEntity updateNameFolder(@Valid @RequestBody FolderUpdateRequest folderUpdateRequest,
                                           Principal principal) {

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String path = folderUpdateRequest.getCurrentPath() == null ? "" : folderUpdateRequest.getCurrentPath();


        if (folderUpdateRequest.getNewNameFolder() == null || folderUpdateRequest.getNewNameFolder().isEmpty() ||
            folderUpdateRequest.getNewNameFolder().equals(folderUpdateRequest.getOldFolderName())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New folder name is invalid or same as old name"));
        }

        try {
            minioService.renameFolder(path, folderUpdateRequest.getOldFolderName(), folderUpdateRequest.getNewNameFolder(), user.getId());
            return ResponseEntity.ok(("from " + folderUpdateRequest.getOldFolderName() + " to " + folderUpdateRequest.getNewNameFolder()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cal");
        }
    }

    //
    @DeleteMapping("/files/delete")
    public ResponseEntity<String> deleteFile(@RequestBody RequestDeleteFile requestDeleteFile,
                                             Principal principal) {

        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String path = requestDeleteFile.getPath() == null ? "" : requestDeleteFile.getPath();

        minioService.delete(requestDeleteFile.getName(), path, user.getId(), requestDeleteFile.isFolder());
        return ResponseEntity.ok(requestDeleteFile.getName() + "deleted successfully");
    }
}



//    @GetMapping("/search/")
//    public String search(@RequestParam("query") String query, Model model,
//                         @AuthenticationPrincipal MyUserDetails userDetails) {
//        Long userId = userDetails.getUserId();
//        List<FileFolderDTO> listLink;
//        listLink = minioService.search(query, userId);
//        model.addAttribute("listLink", listLink);
//        model.addAttribute("nameUser", userDetails.getUsername());
//        return "search";
//    }
////
//        @GetMapping("/download")
//        public ResponseEntity<InputStreamResource> downloadFile (
//                @RequestParam String objectName,
//                Principal principal) throws UnsupportedEncodingException {
//
//            User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//            InputStream inputStream = minioService.downloadFile(objectName, user.getId());
//            String[] ar = objectName.split("/");
//            String nameFile = ar[ar.length - 1];
//            InputStreamResource resource = new InputStreamResource(inputStream);
//            String decodedObjectName = URLEncoder.encode(nameFile, "UTF-8");
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
//                                                             decodedObjectName + "\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        }






