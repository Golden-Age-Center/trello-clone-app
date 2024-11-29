#!/bin/bash
redis-cli --cluster create redis-node-1:7000 redis-node-2:7001 redis-node-3:7002 redis-node-4:7003 redis-node-5:7004 redis-node-6:7005 --cluster-replicas 1 --cluster-yes