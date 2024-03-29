import React from "react";
import { withScriptjs, withGoogleMap, GoogleMap, Marker } from 'react-google-maps';
import { compose, withProps } from 'recompose';
import { MapsApiKey } from './MapsApiKey';

interface MyMapProps {
    lat?: number;
    lng?: number;
    onMapClick: (e: any) => void,
    onMarkerClick: (e: any) => void,
}

export const MyMap =
    compose<MyMapProps, any>(
        withProps({
            googleMapURL:
                `https://maps.googleapis.com/maps/api/js?key=${MapsApiKey}`,
            loadingElement: <div style={{ height: `50%` }} />,
            containerElement: <div style={{ height: `400px` }} />,
            mapElement: <div style={{ height: `100%` }} />
        }),
        withScriptjs,
        withGoogleMap
    )(props => (
        <GoogleMap defaultZoom={8} defaultCenter={{ lat: props.lat, lng: props.lng }} onClick={props.onMapClick}>
            <Marker position={{ lat: props.lat, lng: props.lng }} onClick={props.onMarkerClick}/>
        </GoogleMap>
    ));
