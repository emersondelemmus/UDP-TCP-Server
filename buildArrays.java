    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles_gps;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brendanmcantosh, emersondelemmus
 */
//private List<VehData> ListVehicles = Collections.synchronizedList(new ArrayList<VehData>());
public class buildArrays extends Thread {
    private final List<VehData> All = Collections.synchronizedList(new ArrayList<VehData>());
    private final List<VehData> Active = Collections.synchronizedList(new ArrayList<VehData>());
            
    public void buildArrays(VehData vehicle){
        All.removeIf(All -> All.ident.equals(vehicle.ident));
        All.add(vehicle);
        //System.out.println(All.get(All.size()-1).ID);
    }
    
    public List getAll()
    {
        //System.out.println(All);
        return All;
    }
    public List getActive()
    {
        for (int i = 0; i < All.size(); i++)
        {
            Active.clear();
            if(All.get(i).status.equals("Active"));
                Active.add(All.get(i));
        }
        System.out.println(Active.iterator());
        return Active;
    }

    @Override
    public void run() {
        while(true)
        {
            
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(buildArrays.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println(All.size());
            for (int i = All.size()-1; i >= 0; i--) // If list hasn't been updated in 30 seconds remove entry
            {
                //System.out.println(All.get(i).ID);
                LocalTime t = LocalTime.parse(All.get(i).time);
                float sec;
                sec = t.until(LocalTime.now(),ChronoUnit.SECONDS);
                if (sec > 30)
                    All.remove(i);
                
            }
        }
    }
    
}
