package tv.superawesome.lib.saadloader;

import tv.superawesome.lib.samodelspace.SAAd;

/**
 * Loader interface
 */
public interface SALoaderInterface {

    /**
     * After SALoader pre-loads an Ad, this is the function that should be called
     * @param ad - sends back the Ad object that was loaded
     */
    void didLoadAd(SAAd ad);

}