package imagebrowser.controller;

import imagebrowser.modal.FolderImages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



import javax.imageio.ImageIO;

public class ImageBrowserController {

	public void loadImage(File directory) {
        File[] directoryListing = directory.listFiles();
        FolderImages.getImageFiles().clear();
        FolderImages.setIndex(-1);

        for (File child : directoryListing) {
            // Do something with child
            try {
                BufferedImage image = ImageIO.read(child);
                if (image == null) {
                    continue;
                }
                FolderImages.getImageFiles().add(child.getAbsolutePath());        
            } catch (IOException ex) {
                continue;
            }
        }
        if (FolderImages.getImageFiles().size() > 0) {
            FolderImages.setIndex(0);  
        }
    }

	public boolean moveToNextImage() {
        if (FolderImages.getImageFiles().size() > 0) {
            if (!(FolderImages.getIndex() == FolderImages.getImageFiles().size() - 1)) {
                FolderImages.setIndex(FolderImages.getIndex() + 1);
                return true;
            }
        }
        
        return false;
    }

	public boolean moveToPreviousImage() {
        if (FolderImages.getImageFiles().size() > 0) {
            if (FolderImages.getIndex() > 0) {
                FolderImages.setIndex(FolderImages.getIndex() - 1);
                return true;
            }
        }
        return false;
    }
    
	public void setCurrentImage(int index) {
        if (FolderImages.getImageFiles().size() > 0 && index>=0 && index<FolderImages.getImageFiles().size()) {
        	FolderImages.setIndex(index);
        }
    }
	
	public String getCurrentImage(){
		if(FolderImages.getImageFiles().size() > 0){
			return FolderImages.getImageFiles().get(FolderImages.getIndex());
		}
		
		return "";
	}

}
