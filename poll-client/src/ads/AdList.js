import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import AdPage from './AdPage';
import { POLL_LIST_SIZE } from '../constants';
import { getAllAds, getMyAds } from '../util/APIUtils';

import LoadingIndicator from '../common/LoadingIndicator';
import { Button, Icon, notification, Divider } from 'antd';


class AdList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            ads: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false,
            adViews: []
        };

        this.loadAdList = this.loadAdList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadAdList(page = 0, size = POLL_LIST_SIZE) {
        let promise;

        this.setState({
            isLoading: true
        });

        if (this.props.currentUser && this.props.mode ) {
            promise = getMyAds().then(response => {



                this.setState({
                    ads: response,
                    isLoading: false
                })


                let tempAdViews = []
                this.state.ads.forEach((ad, adIndex) => {
                    tempAdViews.push(<div><AdPage
                        key={ad.id}
                        ad={ad}
                        currentUser={this.props.currentUser}
                        preloaded="true" /><Divider /></div>)
                });

                this.setState({
                    adViews: tempAdViews
                })

            }).catch(error => {
                this.setState({
                    isLoading: false
                })
            });
        }
        else {
            promise = getAllAds(page, size).then(response => {
                const ads = this.state.ads.slice();
                this.setState({
                    ads: ads.concat(response.content),
                    page: response.page,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    isLoading: false
                })

                let tempAdViews = []
                this.state.ads.forEach((ad, adIndex) => {
                    tempAdViews.push(<div><AdPage
                        key={ad.id}
                        ad={ad}
                        currentUser={this.props.currentUser}
                        preloaded="true" /><Divider /></div>)
                });

                this.setState({
                    adViews: tempAdViews
                })
            }).catch(error => {
                this.setState({
                    isLoading: false
                })
            });
        }

        if (!promise) {
            return;
        }

    }

    componentDidMount() {
        this.loadAdList();
    }

    handleLoadMore() {
        this.loadAdList(this.state.page + 1);
    }

    render() {


        return (
            <div className="ads-container">
                {this.state.adViews}
                {
                    !this.state.isLoading && this.state.ads.length === 0 ? (
                        <div className="no-polls-found">
                            <span>No ads Found.</span>
                        </div>
                    ) : null
                }
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-polls">
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>) : null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator /> : null
                }
            </div>
        );
    }
}

export default withRouter(AdList)