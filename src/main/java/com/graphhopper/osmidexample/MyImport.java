/*
 *  Licensed to GraphHopper and Peter Karich under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.osmidexample;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.VirtualEdgeIteratorState;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.EdgeIteratorState;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start with the following command line settings:
 *
 * config=config.properties osmreader.osm=area.pbf
 *
 * @author Peter Karich
 */
public class MyImport {

    public static void main(String[] args) {
        MyGraphHopper graphHopper = new MyGraphHopper();
        graphHopper.init(CmdArgs.read(args));
        graphHopper.importOrLoad();

        Logger logger = LoggerFactory.getLogger(MyImport.class);
        logger.info("edge 30 -> " + graphHopper.getOSMWay(30) + ", " + graphHopper.getGraphHopperStorage().getEdgeIteratorState(30, Integer.MIN_VALUE).fetchWayGeometry(2));

        GHResponse rsp = new GHResponse();
        List<Path> paths = graphHopper.calcPaths(new GHRequest(52.498668, 13.431473, 52.48947, 13.404007).
                setWeighting("fastest").setVehicle("car"), rsp);
        Path path0 = paths.get(0);
        for (EdgeIteratorState edge : path0.calcEdges()) {
            int edgeId = edge.getEdge();
            String vInfo = "";
            if (edge instanceof VirtualEdgeIteratorState) {
                // first, via and last edges can be virtual
                VirtualEdgeIteratorState vEdge = (VirtualEdgeIteratorState) edge;
                edgeId = vEdge.getOriginalTraversalKey() / 2;
                vInfo = "v";
            }

            logger.info("(" + vInfo + edgeId + ") " + graphHopper.getOSMWay(edgeId) + ", " + edge.fetchWayGeometry(2));
        }
    }
}
