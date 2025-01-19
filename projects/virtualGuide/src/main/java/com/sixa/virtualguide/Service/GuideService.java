package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.dto.GuideDetailsDTO;
import com.sixa.virtualguide.dto.GuideListingDTO;
import com.sixa.virtualguide.dto.PasswordUpdateModel;
import com.sixa.virtualguide.dto.ProfileUpdateModel;
import com.sixa.virtualguide.model.Guide;
import com.sixa.virtualguide.repo.GuideRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class GuideService {

    @Autowired
    private final GuideRepo guideRepo;

    @Autowired
    private final AccountService accountService;

    public GuideService(GuideRepo guideRepo, AccountService accountService) {
        this.guideRepo = guideRepo;
        this.accountService = accountService;
    }

    public Optional<Guide> getGuideByEmail(String email) {
        return guideRepo.findByEmail(email);
    }

    public Guide saveGuide(Guide guide) {
        return guideRepo.save(guide);
    }

    public void updateGuideFields(Guide guide, ProfileUpdateModel dto) {
        if (guide == null || dto == null) {
            throw new IllegalArgumentException("Guide and updated data cannot be null");
        }

        // Check if email is being updated and if it's already in use
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !dto.getEmail().equals(guide.getEmail())) {
            if (accountService.isEmailTaken(dto.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            guide.getAccount().setEmail(dto.getEmail());
            guide.setEmail(dto.getEmail());
        }

        // Update other fields only if they are not null
        if (dto.getFirstName() != null) guide.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) guide.setLastName(dto.getLastName());
        if (dto.getPhone() != null) guide.setPhone(dto.getPhone());
        if (dto.getBio() != null) guide.setBio(dto.getBio());
        if (dto.getLanguages() != null) guide.setLanguages(dto.getLanguages());
        if (dto.getLocation() != null) guide.setLocation(dto.getLocation());
        if (isValidPath(String.valueOf(dto.getPicturePaths())) && dto.getPicturePaths() != null && guide.getPicturePaths().size() <= 5) guide.setPicturePaths(dto.getPicturePaths());
        if (dto.getPricePerHour() != -1 ) guide.setPricePerHour(dto.getPricePerHour());
    }

    private boolean isValidPath(String path) {
        return path != null && path.matches("[a-zA-Z0-9/._-]+");
    }

    public GuideDetailsDTO getGuideDetailsById(int guideId) {
        Optional<Guide> guideOptional = guideRepo.findById(guideId);

        if (guideOptional.isEmpty()) {
            throw new IllegalArgumentException("Guide with ID " + guideId + " not found.");
        }

        Guide guide = guideOptional.get();
        return new GuideDetailsDTO(
                guide.getFirstName(),
                guide.getLastName(),
                guide.getEmail(),
                guide.getPhone(),
                guide.getBio(),
                guide.getLanguages(),
                guide.getLocation(),
                guide.getPricePerHour(),
                guide.getPicturePaths()
        );
    }

    public void updateGuidePassword(Guide guide, PasswordUpdateModel dto) {
        if (guide == null || dto == null) {
            throw new IllegalArgumentException("Guide and updated data cannot be null");
        }

        // Update the password if provided and valid
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashedPassword = accountService.hashPassword(dto.getPassword());
            guide.getAccount().setPassword(hashedPassword);
            guide.setPassword(hashedPassword);
        }
    }

    public void deleteGuide(Guide guide) {
        if (guide == null) {
            throw new IllegalArgumentException("Guide cannot be null");
        }

        // Ensure that the guide has an associated account
        if (guide.getAccount() != null) {
            // Explicitly delete the associated account
            accountService.deleteAccount(guide.getAccount());
        }

        // Now delete the guide
        guideRepo.delete(guide);
    }

    public Page<GuideListingDTO> searchGuidesWithPaging(String keyword, Integer minPrice, Integer maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Guide> guidesPage = guideRepo.findGuidesWithPaging(keyword, minPrice, maxPrice, pageable);

        return guidesPage.map(guide -> new GuideListingDTO(
                guide.getGuideId(),
                guide.getPicturePaths(),
                guide.getFirstName(),
                guide.getLastName(),
                guide.getBio(),
                guide.getPricePerHour(),
                guide.getLocation()
        ));
    }

    public Page<GuideListingDTO> getAllGuideListingsWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Guide> guidesPage = guideRepo.findAll(pageable);

        return guidesPage.map(guide -> new GuideListingDTO(
                guide.getGuideId(),
                guide.getPicturePaths(),
                guide.getFirstName(),
                guide.getLastName(),
                guide.getBio(),
                guide.getPricePerHour(),
                guide.getLocation()
        ));
    }

    public Page<GuideListingDTO> getGuideListingsOrderedByPriceWithPaging(String order, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Guide> guidesPage;

        if ("desc".equalsIgnoreCase(order)) {
            guidesPage = guideRepo.findAllByOrderByPricePerHourDesc(pageable);
        } else {
            guidesPage = guideRepo.findAllByOrderByPricePerHourAsc(pageable);
        }

        return guidesPage.map(guide -> new GuideListingDTO(
                guide.getGuideId(),
                guide.getPicturePaths(),
                guide.getFirstName(),
                guide.getLastName(),
                guide.getBio(),
                guide.getPricePerHour(),
                guide.getLocation()
        ));
    }


}
