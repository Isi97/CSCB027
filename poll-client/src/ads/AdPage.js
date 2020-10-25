import "./AdPage.css"

import React, { Component } from 'react';
import { getAd, getGallery  } from "../util/APIUtils"
import ImageFromBlob from "../general/ImageFromBlob";
import { Carousel, Button} from 'antd';
import EditAd from "./EditAd";
import { withRouter } from 'react-router-dom';

class AdPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            loaded: this.props.preloaded,
            hasImages: false,
            imageList: []
        };

        this.checkImages = this.checkImages.bind(this);
        this.editAdButton = this.editAdButton.bind(this);
    }

    componentDidMount() {
        if (this.props.preloaded == false) {
            let stateChange = this.setState.bind(this);

            getAd(this.props.match.params.aid).then((response) => {
                stateChange({
                    loaded: true,
                    ad: response
                });
            });
        } else {
            this.checkImages();
        }
    }

    checkImages() {
        let stateChange = this.setState.bind(this);

        getGallery(this.props.ad.id).then((response) => {
            let temp = []


            response.forEach(element => {
                

                let srcString = 'data:' + element.type + ';base64,' + element.data;
                temp.push(
                <div className=""><img className="carouselImage" src={srcString}></img></div>
                );
                
            });


            stateChange({
                hasImages: true,
                imageList: temp
            });
        });
    }

    editAdButton(event)
    {
        this.props.history.push("/ads/edit/"+this.props.ad.id);
    }



    render() {
        if (this.state.loaded) {
            if (this.props.preloaded) {
                return (
                    <div className="ad-container">
                        <h2 className="ad-title">{this.props.ad.title}</h2>
                        <div className="ad-description">{this.props.ad.description}</div>
                        
                        <Carousel autoplay>
                            {this.state.imageList}
                        </Carousel>
                        <div className="ad-created">Posted on {this.props.ad.createdAt.slice(0, 10)} by {this.props.ad.user.name}</div>
                        <br></br>
                        <div>
                            <p>Address: {this.props.ad.user.contactInformation.address}</p>
                            <p>Phone: {this.props.ad.user.contactInformation.phone}</p>
                        </div>

                        {this.props.currentUser.id == this.props.ad.user.id ? <Button onClick={this.editAdButton} >Edit Ad</Button>:""}
                    </div>
                );
            }
            else {
                return (
                    <div className="ad-container">
                        <h2>{this.state.ad.title}</h2>
                        <div className="ad-descritpion">{this.state.ad.description}</div>
                        <div className="ad-created">Posted on {this.state.ad.createdAt.slice(0, 10)} by {this.state.ad.user.username}</div>
                    </div>
                );
            }

        } else {
            return (<p>nothing to display</p>);
        }
    }
}

export default withRouter(AdPage)