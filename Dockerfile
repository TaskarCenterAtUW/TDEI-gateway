FROM devopsfaith/krakend:2.4.3 as builder
ARG ENV=dev

COPY krakend.tmpl .
COPY config .

## Save temporary file to /tmp to avoid permission errors
RUN FC_ENABLE=1 \
    FC_OUT=/tmp/krakend.json \
    FC_PARTIALS="/etc/krakend/partials" \
    FC_SETTINGS="/etc/krakend/settings/$ENV" \
    FC_TEMPLATES="/etc/krakend/templates" \
    krakend check -d -t -c krakend.tmpl

# The linting needs the final krakend.json file
RUN krakend check -c /tmp/krakend.json --lint

FROM golang:1.20.6 AS plugin-build-stage

WORKDIR /app
COPY plugins ./
RUN go mod tidy
RUN go build -buildmode=plugin -o tdei-plugin.so .


FROM devopsfaith/krakend:2.4.3
COPY --from=builder /tmp/krakend.json .
COPY --from=plugin-build-stage /app/tdei-plugin.so ./tdei-plugin/
RUN chmod 777 krakend.json

EXPOSE 8080

CMD [ "run", "-c", "/etc/krakend/krakend.json" ]
