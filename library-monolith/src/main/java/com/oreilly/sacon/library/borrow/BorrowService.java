package com.oreilly.sacon.library.borrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {

    @Autowired
    private ItemAvailabilityRepository itemAvailabilityRepository;

    public ItemAvailability getAvailability(long itemId) {
        return itemAvailabilityRepository.findOne(itemId);
    }

    public void changeAvailability(long itemId) {
        ItemAvailability itemAvailabilityToChange = itemAvailabilityRepository.findOne(itemId);
        itemAvailabilityToChange.setAvailable(!itemAvailabilityToChange.isAvailable());
        itemAvailabilityRepository.save(itemAvailabilityToChange);
    }
}
