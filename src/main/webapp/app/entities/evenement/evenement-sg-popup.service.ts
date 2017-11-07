import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { EvenementSg } from './evenement-sg.model';
import { EvenementSgService } from './evenement-sg.service';

@Injectable()
export class EvenementSgPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private evenementService: EvenementSgService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, idSphere?: number ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.evenementService.find(id).subscribe((evenement) => {
                    evenement.date = this.datePipe
                        .transform(evenement.date, 'yyyy-MM-ddThh:mm');
                    evenement.dateFinVote = this.datePipe
                        .transform(evenement.dateFinVote, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.evenementModalRef(component, evenement);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.evenementModalRef(component, new EvenementSg({sphereId: idSphere}));
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    evenementModalRef(component: Component, evenement: EvenementSg): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.evenement = evenement;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
