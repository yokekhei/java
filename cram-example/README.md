# Challenge Response Authentication Mechanism Example

The Challenge Response Authentication Mechanism (CRAM) example application demonstrates the authentication process between front-end and back-end applications.
  
Front-end application applies RSA key-pair generator algorithm to create public and private key, and register its public key to back-end application.
  
Back-end application provides challenge code to front-end application.

Front-end application resolves challenge and compute response code as the input of signature.

Both front-end and back-end application applies SHA256withRSA signature algorithm to create and verify signature respectively. 

