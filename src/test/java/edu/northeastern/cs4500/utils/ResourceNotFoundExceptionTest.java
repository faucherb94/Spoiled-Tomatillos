package edu.northeastern.cs4500.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import edu.northeastern.cs4500.models.User;

@RunWith(SpringRunner.class)
public class ResourceNotFoundExceptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void toMap_oddNumberOfArguments() throws Exception {
        throw new ResourceNotFoundException(User.class, "id");
    }
}