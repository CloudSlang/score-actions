package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import io.cloudslang.content.jclouds.utils.OutputsUtil;
import org.jclouds.ec2.domain.Image;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 5/6/2016.
 */
public class DescribeImagesInRegionExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, ImageInputs imageInputs) throws Exception {
        ImageService imageService = ImageFactory.getImageService(commonInputs);

        Set<? extends Image> response = imageService.describeImagesInRegion(commonInputs, imageInputs);

        return OutputsUtil.getResultsMap(response.toString());
    }
}