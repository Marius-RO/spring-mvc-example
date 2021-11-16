package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.dto.CategoryDto;
import com.company.model.Category;
import com.company.service.CategoryService;
import com.company.util.GeneralUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(path = CategoryController.PathHandler.BASE_URL)
public class CategoryController extends AbstractController {

    public interface ViewHandler {
        String CATEGORIES = "categories";
        String ADD_CATEGORY = "category-add";
        String UPDATE_CATEGORY = "category-update";
    }

    public interface PathHandler {
        String BASE_URL = "/categories";
        String CATEGORIES_URL = "/all";
        String FULL_CATEGORIES_URL = BASE_URL + CATEGORIES_URL;
        String ADD_CATEGORY_URL = "/add";
        String UPDATE_CATEGORY_URL = "/update";
        String FULL_UPDATE_CATEGORY_URL = BASE_URL + UPDATE_CATEGORY_URL;
        String PROCESS_ADD_CATEGORY_URL = "/process-add";
        String PROCESS_UPDATE_CATEGORY_URL = "/process-update";
        String PROCESS_DELETE_CATEGORY_URL = "/process-delete";
    }

    public interface Security {
        String[] PERMIT_ONLY_ADMIN = {
                PathHandler.BASE_URL + "/*",
        };
    }

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(WebApplicationContext webApplicationContext, CategoryService categoryService) {
        super(webApplicationContext);
        this.categoryService = categoryService;
    }

    @Override
    protected Class<?> getClassType() {
        return CategoryController.class;
    }

    @RequestMapping(path = PathHandler.CATEGORIES_URL, method = RequestMethod.GET)
    public String getCategoriesPage(Model model){
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return ViewHandler.CATEGORIES;
    }

    @RequestMapping(path = PathHandler.ADD_CATEGORY_URL, method = RequestMethod.GET)
    public String getAddCategoryPage(@ModelAttribute("categoryDto") CategoryDto categoryDto){
        return ViewHandler.ADD_CATEGORY;
    }

    @RequestMapping(path = PathHandler.PROCESS_ADD_CATEGORY_URL, method = RequestMethod.POST)
    public String processAddCategory(@Valid CategoryDto categoryDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return ViewHandler.ADD_CATEGORY;
        }

        categoryService.addCategory(categoryDto);
        model.addAttribute("attributeSuccessAddCategory", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_CATEGORIES_URL;
    }

    @RequestMapping(path = PathHandler.UPDATE_CATEGORY_URL + "/{id}", method = RequestMethod.GET)
    public String getUpdateCategoryPage(@PathVariable("id") int categoryId, Model model){
        Category category = categoryService.getCategoryById(categoryId);

        // https://stackoverflow.com/questions/2543797/spring-redirect-after-post-even-with-validation-errors
        // If page is redirected from PROCESS_UPDATE_CATEGORY_URL means that some binding errors occurred, so model
        // wil contain already a 'categoryDto' attribute. If model does not contain such attribute means that request is
        // from another page and this must be added and linked with DB info.
        if(!model.containsAttribute("categoryDto")){
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());
            model.addAttribute("categoryDto", categoryDto);
        }

        model.addAttribute("categoryId", categoryId);
        return ViewHandler.UPDATE_CATEGORY;
    }

    @RequestMapping(path = PathHandler.PROCESS_UPDATE_CATEGORY_URL + "/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String processUpdateCategory(@PathVariable("id") int categoryId,
                                        @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                                        BindingResult bindingResult,
                                        Model model,
                                        RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            // https://stackoverflow.com/questions/2543797/spring-redirect-after-post-even-with-validation-errors
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryDto", bindingResult);
            redirectAttributes.addFlashAttribute("categoryDto", categoryDto);
            return GeneralUtilities.REDIRECT + PathHandler.FULL_UPDATE_CATEGORY_URL + "/" + categoryId;
        }

        categoryService.updateCategoryById(categoryDto, categoryId);
        model.addAttribute("attributeSuccessUpdateCategory", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_CATEGORIES_URL;
    }

    @RequestMapping(path = PathHandler.PROCESS_DELETE_CATEGORY_URL + "/{id}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> processDeleteCategory(@PathVariable("id") int categoryId){
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
