// ILocationService.aidl
package com.liu.jim;

// Declare any non-default types here with import statements
import com.liu.jim.locator.Location;
import com.liu.jim.locator.LocationListener;

interface ILocationService {

   void getLocation(LocationListener locationlistener);


}
