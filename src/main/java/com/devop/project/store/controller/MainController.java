package com.devop.project.store.controller;

import com.devop.project.store.category.CategoryRepository;
import com.devop.project.store.category.ProductRepository;
import com.devop.project.store.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Controller
@Slf4j
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @ModelAttribute
    public void addCategories(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/listProduct")
    public String listProduct(@RequestParam(name = "name", defaultValue = "", required = false)
                                          String productNameToSearch, Model model){
        Collection<Product> products;

        if (productNameToSearch.isEmpty()){
            products = productRepository.findAllSorted();
        } else {
            logger.info("parametro ingresado: {}", productNameToSearch);
            ///products = productRepository.searchProduct(productNameToSearch);
            products = productRepository.findByNameContainingIgnoreCase(productNameToSearch);
        }

        if (products.isEmpty()){
            model.addAttribute("products", productRepository.findAllSorted());
            model.addAttribute("product", new Product());
            model.addAttribute("productNameToSearch", productNameToSearch);
            return "listProduct";
        } else  if (products.size() == 1) {
            return "redirect:/product/"+products.iterator().next().getId();
        } else {
            model.addAttribute("products", products);
            model.addAttribute("product", new Product());
            model.addAttribute("productNameToSearch", productNameToSearch);
            return "listProduct";
        }
    }

    @GetMapping("/product")
    public String product(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return "viewProductForm";
    }

    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable Long id, Model model){
        model.addAttribute("product", getProducto(id));
        //model.addAttribute("categories", categoryRepository.findAll());
        return "viewProductForm";
    }

    private Product getProducto(Long id){
        Optional<Product> product = productRepository.findById(id);
        return product.isPresent()? product.get() : new Product();
    }

    @GetMapping("/newProduct")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        //model.addAttribute("categories", categoryRepository.findAll());
        return "newProductForm";
    }

    @PostMapping("/createProduct")
    public String createProduct(@Valid Product product, BindingResult result, Model model){
        if (result.hasErrors()){
            logger.info("Se tienen errores");
            return "newProductForm";
        } else if (!productRepository.existsProductByName(product.getName())){
            logger.info("Creando producto");
            productRepository.save(product);
            return "redirect:/listProduct";
        } else {
            logger.info("El nombre del producto ya existe");
            result.rejectValue("name", "product.name.duplicated", "El nombre del producto ya existe");
            return "newProductForm";
        }
    }

    @PostMapping("/editProduct")
    public String editProduct(@Valid Product product, BindingResult result){
        logger.info("Edit product: {}", product.getId());
        if (result.hasErrors()){
            return "viewProductForm";
        } else if (!productRepository.existsProductByNameAndIdNot(product.getName(), product.getId())){
            productRepository.save(product);
            return "redirect:/listProduct";
        } else {
            logger.info("El nombre del producto ya existe");
            result.rejectValue("name", "product.name.duplicated", "El nombre del producto ya existe");
            return "viewProductForm";
        }
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id){
        productRepository.deleteById(id);
        return "redirect:/listProduct";
    }

}
