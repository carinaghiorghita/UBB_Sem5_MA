import { createAnimation, CreateAnimation, IonChip, IonContent, IonFab, IonFabButton, IonHeader, IonIcon, IonInfiniteScroll, IonInfiniteScrollContent, IonItem, IonLabel, IonList, IonLoading, IonPage, IonSearchbar, IonSelect, IonSelectOption, IonToast, IonToolbar } from "@ionic/react";
import React, { useContext, useEffect, useState } from "react";
import { RouteComponentProps } from "react-router";
import { BookContext } from "./BookProvider";
import Book from "./Book";
import {add} from 'ionicons/icons';
import { AuthContext } from "../auth";
import { BookProps } from "./BookProps";
import { getLogger } from '../../core';
import {Network} from '@capacitor/core';
import './Book.css';

const log = getLogger('BookList');

const offset = 3;

const BookList : React.FC<RouteComponentProps> = ({history}) => {
    const { logout } = useContext(AuthContext)
    const {items, fetching, fetchingError} = useContext(BookContext);
    const [disableInfiniteScroll, setDisabledInfiniteScroll] = useState<boolean>(false);
    const [visibleItems, setVisibleItems] = useState<BookProps[] | undefined>([]);
    const [page, setPage] = useState(offset)
    const [filter, setFilter] = useState<string | undefined>(undefined);
    const [search, setSearch] = useState<string>("");
    const [status, setStatus] = useState<boolean>(true);

    Network.getStatus().then(status => setStatus(status.connected));

    Network.addListener('networkStatusChange', (status) => {
        setStatus(status.connected);
    })

    const owned = ['true','false','all'];

    useEffect(() => {
        if (items?.length && items?.length > 0) {
            setPage(offset);
            fetchData();
            console.log(items);
        }
    }, [items]);

    useEffect(() => {
        if (items && filter) {
            if(filter === 'all')
                setVisibleItems(items);
            else
                setVisibleItems(items.filter(each => each.owned.toString() === filter));
        }
    }, [filter]);

    useEffect(() => {
        if (search === "") {
            setVisibleItems(items);
        }
        if (items && search !== "") {
            setVisibleItems(items.filter(each => each.title.toLowerCase().includes(search.toLowerCase())));
        }
    }, [search])

    function fetchData() {
        setVisibleItems(items?.slice(0, page));
        setPage(page + offset);
        if (items && page > items?.length) {
            setDisabledInfiniteScroll(true);
            setPage(items.length);
        } else {
            setDisabledInfiniteScroll(false);
        }
    }

    async function searchNext($event: CustomEvent<void>) {
        fetchData();
        ($event.target as HTMLIonInfiniteScrollElement).complete();
    }

// animations

    const title = document.querySelector('.title');
    if (title) {
        const titleAnimation = createAnimation()
            .addElement(title)
            .duration(2000)
            .direction('alternate')
            .iterations(Infinity)
            .keyframes([
                { offset: 0, opacity: '0.2' },
                { offset: 0.5, opacity: '1' },
                { offset: 1, opacity: '0.2' }
            ]);
        titleAnimation.play();
    }


    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonItem>
                        <IonSelect style={{ width: '40%' }} value={filter} placeholder="Filter by owned:" onIonChange={(e) => setFilter(e.detail.value)}>
                            {owned.map((each) => (
                                <IonSelectOption key={each} value={each}>
                                        {each}
                                </IonSelectOption>
                            ))}
                        </IonSelect>
                        <IonSearchbar style={{ width: '50%' }} placeholder="Search by title:" value={search} debounce={200} onIonChange={(e) => {
                            setSearch(e.detail.value!);
                        }}>
                        </IonSearchbar>
                        <CreateAnimation
                            duration={2000}
                            iterations={Infinity}
                            keyframes={[
                                { offset: 0, transform: 'scale(1)', opacity: '0.5' },
                                { offset: 0.5, transform: 'scale(0.8)', opacity: '1' },
                                { offset: 1, transform: 'scale(1)', opacity: '0.5' }
                            ]}>
                        </CreateAnimation>
                        <IonChip>
                        <IonLabel color={status? "success" : "danger"}>{status? "Online" : "Offline"}</IonLabel>
                    </IonChip>
                    </IonItem>
                </IonToolbar>
            </IonHeader>

            <IonContent fullscreen>
                <br/>
                <span className={`title`}>Books</span>
                <IonLoading isOpen={fetching} message="Loading..."/>

                {
                    visibleItems &&(
                        
                        <IonList>
                            {Array.from(visibleItems)
                                .filter(each => {
                                    if (filter !== undefined && filter !== 'all')
                                        return each.owned.toString() === filter && each._id !== undefined;
                                    return each._id !== undefined;
                                })
                                .map(({_id, title, author, publishingDate, owned, latitude, longitude, webViewPath}) =>
                                <Book key={_id} _id={_id} title={title} author={author} publishingDate={publishingDate} owned={owned || false} latitude={latitude} longitude={longitude} webViewPath={webViewPath} onEdit={_id => history.push(`/api/items/book/${_id}`)} />)}
                        </IonList>
                    )
                }

                <IonInfiniteScroll threshold="100px" disabled={disableInfiniteScroll} onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
                    <IonInfiniteScrollContent loadingText="Loading...">
                    </IonInfiniteScrollContent>
                </IonInfiniteScroll>

                {
                    fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch items'}</div>
                    )
                }

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/api/items/book')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>

                <IonFab vertical="bottom" horizontal="start" slot="fixed">
                    <IonFabButton onClick={handleLogout}>
                        Log out
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );

    function handleLogout() {
        log("logout");
        logout?.();
    }
};


export default BookList;