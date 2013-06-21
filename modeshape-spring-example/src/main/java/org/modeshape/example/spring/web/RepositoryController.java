package org.modeshape.example.spring.web;

import java.util.Set;
import java.util.TreeSet;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC web controller
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
@Controller
public class RepositoryController {

    private static final String MAIN_URL = "main.url";

    private Session session;
    private String repositoryName;

    @Autowired
    public RepositoryController(Session session) {
        this.session = session;
        repositoryName = session.getRepository().getDescriptor(org.modeshape.jcr.api.Repository.REPOSITORY_NAME);
    }

    @ExceptionHandler( RepositoryException.class )
    public ModelAndView handleRepositoryException( RepositoryException e ) {
        return modelAndView(new RepositoryModel().setRepositoryName(repositoryName)).addObject("globalError", e.getMessage());
    }

    @RequestMapping( value = { "/", MAIN_URL }, method = RequestMethod.GET )
    public ModelAndView main() {
        return modelAndView(new RepositoryModel().setRepositoryName(repositoryName));
    }

    /**
     * Loads the children nodes of the node located at "parentPath"
     */
    @RequestMapping( params = "loadChildren", method = RequestMethod.POST )
    public ModelAndView getChildren( @ModelAttribute RepositoryModel repositoryModel,
                                     BindingResult bindingResult ) throws RepositoryException {
        if (repositoryModel.validateParentPath(bindingResult)) {
            Node parentNode = session.getNode(repositoryModel.getParentPath());
            Set<String> children = new TreeSet<String>();
            for (NodeIterator nodeIterator = parentNode.getNodes(); nodeIterator.hasNext(); ) {
                children.add(nodeIterator.nextNode().getPath());
            }
            repositoryModel.setChildren(children);
        }
        return modelAndView(repositoryModel);
    }

    /**
     * Adds a child node at parentPath which has the name newNodeName
     */
    @RequestMapping( params = "addNode", method = RequestMethod.POST )
    public ModelAndView addChildNode( @ModelAttribute RepositoryModel repositoryModel,
                                      BindingResult bindingResult ) throws RepositoryException {
        if (repositoryModel.validateParentPath(bindingResult) && repositoryModel.validateNodeName(bindingResult)) {
            Node parentNode = session.getNode(repositoryModel.getParentPath());
            parentNode.addNode(repositoryModel.getNewNodeName());
            session.save();
            return getChildren(repositoryModel, bindingResult);
        } else {
            return modelAndView(repositoryModel);
        }
    }

    private ModelAndView modelAndView( RepositoryModel model ) {
        return new ModelAndView("main")
                .addObject("repositoryModel", model);
    }
}
