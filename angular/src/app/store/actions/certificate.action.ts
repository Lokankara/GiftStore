import {createAction, props} from "@ngrx/store";
import {ICertificate} from "../../model/entity/ICertificate";

export const getCertificatesPending = createAction(
  '[Certificates] Get certificates pending',
);

export const getCertificatesSuccess = createAction(
  '[Certificates] Get certificates success',
  props<{ certificates: ICertificate[] }>()
);

export const getCertificatesError = createAction(
  '[Certificates] Get certificates error',
);

export const saveCertificatesSuccess = createAction(
  '[Certificates] Save Certificates Success',
  props<{ certificates: ICertificate[] }>()
);
