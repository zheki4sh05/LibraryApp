package ru.digitalchief.LibraryApp.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.digitalchief.LibraryApp.Repository.*;
import ru.digitalchief.LibraryApp.dto.*;
import ru.digitalchief.LibraryApp.entity.*;
import ru.digitalchief.LibraryApp.exceptions.*;

import java.time.*;
import java.util.*;

@Service
public class CatalogService {


    @Autowired
    private CatalogRepository catalogRepository;
    public void save(CatalogDto catalogDto)throws NotUniqueIsbnException {

       // catalogDao.save(mapToEntity(catalogDto));

       Optional<Catalog> currentCatalog =  catalogRepository.getByUsbn(catalogDto.getId());

       if(currentCatalog.isEmpty()){
           catalogRepository.save(mapToEntity(catalogDto));
       }else{
           throw new NotUniqueIsbnException();
       }



    }

    public void update(CatalogDto catalogDto) throws BadRequestException{


        Optional<Catalog> currentCatalog =  catalogRepository.getByUsbn(catalogDto.getId());

        if(currentCatalog.isPresent()){
            catalogDto.setId(currentCatalog.get().getId());
            catalogRepository.save(mapToEntity(catalogDto));
        }else{
            throw new BadRequestException();
        }
    }

    public CatalogDto getByIsbn(Long isbn) throws BadRequestException{
            return mapToDto(catalogRepository.getByUsbn(isbn).orElseThrow(BadRequestException::new));
    }

    public void delete(Long isbn) throws BadRequestException {

        Catalog catalog = catalogRepository.getByUsbn(isbn).orElseThrow(BadRequestException::new);

        catalogRepository.delete(catalog);

    }

    public List<CatalogDto> getByAuthor(String author) throws BadRequestException{

        List<Catalog> catalogList = catalogRepository.findByAuthor(author);

        List<CatalogDto> catalogDtoList = new ArrayList<>();

        catalogList.forEach(item->{
            catalogDtoList.add(mapToDto(item));
        });
        return  catalogDtoList;
    }

    public List<CatalogDto> getByUdk(String udk) throws BadRequestException{

        List<Catalog> catalogList = catalogRepository.findByUdk(udk);

        List<CatalogDto> catalogDtoList = new ArrayList<>();

      catalogList.forEach(item->{
          catalogDtoList.add(mapToDto(item));
      });

      return  catalogDtoList;

    }

    private Catalog mapToEntity(CatalogDto catalogDto){
        return new Catalog(
                catalogDto.getId(),
                catalogDto.getUdk(),
                catalogDto.getName(),
                catalogDto.getAuthor(),
                catalogDto.getPages(),
                LocalDate.ofYearDay(Integer.parseInt(catalogDto.getYear()),1)
        );
    }

    private CatalogDto mapToDto(Catalog catalog){
        return new CatalogDto(
                catalog.getId(),
                catalog.getUdk(),
                catalog.getName(),
                catalog.getAuthor(),
                catalog.getPages(),
                catalog.getYear().toString()
        );
    }

}
