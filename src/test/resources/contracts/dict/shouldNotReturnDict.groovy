package contracts.dict

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        name "shouldNotReturnDict"
        method GET()
        url '/api/v1/dicts/-1'
    }
    response {
        status 404
    }
}