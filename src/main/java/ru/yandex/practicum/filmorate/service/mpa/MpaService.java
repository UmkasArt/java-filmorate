package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MpaService {

    private final MpaDbStorage mpaStorage;


    @Autowired
    public MpaService(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAllMpas() {
        return new ArrayList<>(mpaStorage.getMpas().values());
    }

    public Mpa findMpaById(Integer mpaId) {
        if (!mpaStorage.getMpas().containsKey(mpaId)) {
            throw new NoSuchElementException();
        }
        return mpaStorage.getMpas().get(mpaId);
    }
}
